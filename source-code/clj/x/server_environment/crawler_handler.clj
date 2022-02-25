
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.01.01
; Description:
; Version: v0.6.4
; Compatibility: x4.6.2



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-environment.crawler-handler
    (:require [mid-fruits.candy   :refer [param return]]
              [mid-fruits.time    :as time]
              [server-fruits.http :as http]
              [x.app-details      :as details]
              [x.server-core.api  :as a]
              [x.server-user.api  :as user]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- sitemap-xml
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (string)
  []
  (if-let [sitemap-routes @(a/subscribe [:router/get-sitemap-routes])]
          (letfn [(f [xml url] (str xml "<url><loc>" url "</loc></url>"))]
                 (str "<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">"
                      (reduce f "" sitemap-routes)
                      "</urlset>"))))

(defn download-sitemap-xml
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) request
  ;
  ; @return (map)
  ;  {:body (string)}
  [_]
  (if-let [sitemap-xml (sitemap-xml)]
          (http/xml-wrap   {:body sitemap-xml})
          (http/error-wrap {:error-message "File not found" :status 404})))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- robots-txt
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

(defn download-robots-txt
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) request
  ;
  ; @return (map)
  ;  {:body (string)}
  [request]
  (if-let [robots-txt (robots-txt request)]
          (http/text-wrap  {:body robots-txt})
          (http/error-wrap {:error-message "File not found" :status 404})))



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



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles!
  ::lifecycles
  {:on-server-init [:router/add-routes! {:environment/robots.txt  {:route-template "/robots.txt"
                                                                   :get {:handler download-robots-txt}}
                                         :environment/sitemap.xml {:route-template "/sitemap.xml"
                                                                   :get {:handler download-sitemap-xml}}}]})
