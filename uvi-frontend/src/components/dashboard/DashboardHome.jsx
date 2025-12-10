import AdminDashboardHome from './AdminDashboardHome';
import DocenteDashboardHome from './DocenteDashboardHome';
import AlumnoDashboardHome from './AlumnoDashboardHome';

const DashboardHome = () => {
    const rol = localStorage.getItem('rol');

    if (rol === 'ADMIN') {
        return <AdminDashboardHome />;
    } else if (rol === 'DOCENTE') {
        return <DocenteDashboardHome />;
    } else {
        return <AlumnoDashboardHome />;
    }
};

export default DashboardHome;