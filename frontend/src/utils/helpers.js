export const formatDate = (dateString) => {
  if (!dateString) return '';
  const date = new Date(dateString);
  return date.toLocaleDateString('en-US', { 
    year: 'numeric', 
    month: 'short', 
    day: 'numeric' 
  });
};

export const formatDateTime = (dateString) => {
  if (!dateString) return '';
  const date = new Date(dateString);
  return date.toLocaleString('en-US', { 
    year: 'numeric', 
    month: 'short', 
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  });
};

export const formatDateForInput = (dateString) => {
  if (!dateString) return '';
  const date = new Date(dateString);
  return date.toISOString().slice(0, 16);
};

export const getStatusColor = (status) => {
  const colors = {
    UPCOMING: '#3b82f6',
    IN_PROGRESS: '#f59e0b',
    COMPLETED: '#10b981',
    CANCELLED: '#ef4444',
    POSTPONED: '#6b7280',
    SCHEDULED: '#3b82f6',
  };
  return colors[status] || '#6b7280';
};

export const getStatusLabel = (status) => {
  return status.replace(/_/g, ' ').toLowerCase().replace(/\b\w/g, l => l.toUpperCase());
};
