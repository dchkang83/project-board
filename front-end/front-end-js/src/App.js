import React from 'react';

import RootRoutes from '~/routes';

const App = () => {
  return (
    <React.Suspense>
      <RootRoutes />
    </React.Suspense>
  );
}

export default App;