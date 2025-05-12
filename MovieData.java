    // This would be implemented with your actual backend
    public class MovieData {
        private String title;
        private String posterUrl;
        
        public MovieData(String title, String posterUrl) {
            this.title = title;
            this.posterUrl = posterUrl;
        }
        
        public String getTitle() {
            return title;
        }
        
        public String getPosterUrl() {
            return posterUrl;
        }
    }
