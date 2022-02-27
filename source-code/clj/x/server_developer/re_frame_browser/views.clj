
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-developer.re-frame-browser.views
    (:require [mid-fruits.map     :as map]
              [mid-fruits.reader  :as reader]
              [mid-fruits.vector  :as vector]
              [server-fruits.http :as http]
              [x.server-core.api  :as a]
              [x.server-developer.re-frame-browser.styles :as re-frame-browser.styles]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- up-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [path]}]
  (if (vector/nonempty? path)
      (let [parent-path (vector/pop-last-item path)]
           (str "<div><a href=\"?path=" parent-path "\">..</a></div>"))
      (str "<div>..</div>")))

(defn- map-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [path]} k v]
  (let [target-path (conj path k)]
       (str "<div><a href=\"?path=" target-path "\">" k "</a></div>")))

(defn- map-items
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [current-item] :as browser-props}]
  (letfn [(f [o k] (str o (map-item browser-props k (k current-item))))]
         (let [keys (-> current-item map/get-keys vector/abc-items)]
              (reduce f "" keys))))

(defn- re-frame-browser
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [current-item] :as browser-props}]
  (str (up-button browser-props)
       (cond (map?    current-item) (map-items browser-props)
             (nil?    current-item) (str "nil")
             (string? current-item) (str "\"" current-item "\"")
             :else                  (str      current-item))))

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [query-params]}]
  (let [db          @(a/subscribe [:db/get-db])
        path         (reader/string->mixed (get query-params "path" "[]"))
        current-item (get-in db path)]
       (re-frame-browser {:current-item current-item :path path})))
