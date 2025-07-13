import { defineConfig } from 'vite';

export default defineConfig({
  server: {
    host: '0.0.0.0',
    port: 4200,
    strictPort: true,
    hmr: {
      clientPort: 4200
    },
    allowedHosts: 'all'
  }
});

