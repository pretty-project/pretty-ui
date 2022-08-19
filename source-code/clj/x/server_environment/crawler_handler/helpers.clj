
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-environment.crawler-handler.helpers
    (:require [mid-fruits.candy   :refer [param return]]
              [mid-fruits.time    :as time]
              [x.app-details      :as details]
              [x.server-core.api  :as a]
              [x.server-user.api  :as user]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn sitemap-xml
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (string)
  []
  (if-let [sitemap-routes @(a/subscribe [:router/get-sitemap-routes])]
          (letfn [(f [xml url] (str xml "<url><loc>" url "</loc></url>"))]
                 (str "<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">"
                      (reduce f "" sitemap-routes)
                      "</urlset>"))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn robots-txt
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) request
  ;  {:server-name (string)}
  ;
  ; @return (string)
  [{:keys [server-name]}]
  (let [app-home @(a/subscribe [:router/get-app-home])
        timestamp (time/timestamp-string)]
       (str "\n# Generated by: " details/app-codename
            "\n# Generated at: " timestamp "\n"
            "\nUser-agent: *\nAllow: /\nDisallow: " app-home "\n"
            (if-let [sitemap-xml (sitemap-xml)]
                    (str "\nSitemap: https://" server-name "/sitemap.xml")))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn crawler-rules
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; A <meta name="robots"> tag content attribútumának értéke
  ;
  ; index:   Show this page in search results
  ; follow:  Follow the links on this page
  ; archive: Show a cached link in search results
  ; snippet: Show a text snippet or video preview in the search results for this page
  ;
  ; @param (map) request
  ;
  ; @return (string)
  [request]
  (if (user/request->authenticated? request)
      (return "noindex, nofollow, noarchive, nosnippet")
      (return "index, follow, noarchive, max-snippet:200, max-image-preview:large")))
