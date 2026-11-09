import { useEffect, useState } from 'react';
import './OrderHistory.css';
import { latestOrder, summarizeOrders } from '../../service/OrderService';

const PAGE_SIZE = 2;

const hasNextPage = (pageData, loadedOrdersCount) => {
  if (typeof pageData.last === 'boolean') {
    return !pageData.last;
  }

  return loadedOrdersCount === PAGE_SIZE;
};

const OrderHistory = () => {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(false);
  const [loadingMore, setLoadingMore] = useState(false);
  const [pageNo, setPageNo] = useState(0);
  const [hasMoreOrders, setHasMoreOrders] = useState(false);
  const [searchValue, setSearchValue] = useState("");
  const [summary, setSummary] = useState("");
  const [summaryLoading, setSummaryLoading] = useState(false);

  useEffect(() => {
    const fetchOrders = async () => {
      try {
        setLoading(true);
        const response = await latestOrder(0, PAGE_SIZE, searchValue);
        console.log("response.data from latest order", response.data);
        const fetchedOrders = response.data.content || [];

        setOrders(fetchedOrders);
        setPageNo(response.data.number || 0);
        setHasMoreOrders(hasNextPage(response.data, fetchedOrders.length));
      } catch (e) {
        console.log(e);
      } finally {
        setLoading(false);
      }
    };

    fetchOrders();
  }, [searchValue]);

  const handleViewMore = async () => {
    try {
      setLoadingMore(true);
      const nextPage = pageNo + 1;
      const response = await latestOrder(nextPage, PAGE_SIZE, searchValue);
      const nextOrders = response.data.content || [];

      setOrders((prevOrders) => [...prevOrders, ...nextOrders]);
      setPageNo(response.data.number ?? nextPage);
      setHasMoreOrders(hasNextPage(response.data, nextOrders.length));
    } catch (e) {
      console.log(e);
    } finally {
      setLoadingMore(false);
    }
  };

  const handleSummarize = async () => {
    try {
      setSummaryLoading(true);
      const response = await summarizeOrders();
      setSummary(response.data);
    } catch (e) {
      console.log(e);
      setSummary("Failed to generate summary.");
    } finally {
      setSummaryLoading(false);
    }
  };

  const formatItems = (items = []) => {
    return items.map((item) => `${item.name} x ${item.quantity}`).join(', ');
  };

  const formatDate = (dateString) => {
    const options = {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
    };
    return new Date(dateString).toLocaleDateString('en-US', options);
  };

  return (
    <div className="orders-history-container">
      <h2 className="mb-3 text-light">All Orders</h2>

      <div className="orders-toolbar mb-3">
        <div className="input-group orders-search">
          <input
            type="text"
            className="form-control"
            placeholder="Search orders..."
            value={searchValue}
            onChange={(e) => setSearchValue(e.target.value)}
          />
          <span className="input-group-text bg-warning">
            <i className="bi bi-search"></i>
          </span>
        </div>

        <button
          className="btn btn-primary"
          onClick={handleSummarize}
          disabled={summaryLoading || loading || orders.length === 0}
        >
          {summaryLoading ? "Generating..." : "Summarize Orders"}
        </button>
      </div>

      {loading && (
        <div className="text-center py-4 text-light">Loading orders...</div>
      )}

      {!loading && orders.length === 0 && (
        <div className="text-center py-4 text-light">No order found</div>
      )}

      {!loading && orders.length > 0 && (
        <>
          <div className="table-responsive">
            <table className="table table-striped table-hover">
              <thead className="table-dark">
                <tr>
                  <th>Order Id</th>
                  <th>Customer</th>
                  <th>Items</th>
                  <th>Total</th>
                  <th>Payment</th>
                  <th>Status</th>
                  <th>Date</th>
                </tr>
              </thead>
              <tbody>
                {orders.map((order) => (
                  <tr key={order.orderId}>
                    <td>{order.orderId}</td>
                    <td>
                      {order.customerName} <br />
                      <small className="text-muted">{order.phoneNumber}</small>
                    </td>
                    <td>{formatItems(order.items)}</td>
                    <td>₹{order.grandTotal}</td>
                    <td>{order.paymentMethod}</td>
                    <td>
                      <span
                        className={`badge ${
                          order.paymentDetails?.status === 'COMPLETED'
                            ? 'bg-success'
                            : 'bg-warning text-dark'
                        }`}
                      >
                        {order.paymentDetails?.status || 'PENDING'}
                      </span>
                    </td>
                    <td>{formatDate(order.createdAt)}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>

          {hasMoreOrders && (
            <div className="d-flex justify-content-center mt-3">
              <button
                className="btn btn-outline-light view-more-orders-btn"
                onClick={handleViewMore}
                disabled={loadingMore}
              >
                {loadingMore ? "Loading..." : "View More"}
              </button>
            </div>
          )}
        </>
      )}

      {summary && (
        <section className="summary-wrapper mt-4" aria-label="Order summary">
          <div className="summary-card shadow">
            <div className="summary-header">
              <div>
                <span className="summary-label">AI Summary</span>
                <h5>Historical Order Summary</h5>
              </div>
              <button
                type="button"
                className="btn btn-sm btn-outline-light"
                onClick={() => setSummary("")}
              >
                Close
              </button>
            </div>
            <div className="summary-content">{summary}</div>
          </div>
        </section>
      )}
    </div>
  );
};

export default OrderHistory;
