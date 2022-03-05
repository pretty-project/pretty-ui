
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-developer.re-frame-browser.views
    (:require [mid-fruits.map                             :as map]
              [mid-fruits.reader                          :as reader]
              [mid-fruits.vector                          :as vector]
              [server-fruits.http                         :as http]
              [x.server-core.api                          :as a]
              [x.server-developer.re-frame-browser.styles :as re-frame-browser.styles]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- up-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [path]}]
  (if (vector/nonempty? path)
      (let [parent-path       (vector/pop-last-item path)
            menu-button-style (re-frame-browser.styles/menu-button-style)]
           (str "<div><a style=\""menu-button-style"\" href=\"?path=" parent-path "\">..</a></div>"))
      (let [menu-button-style (re-frame-browser.styles/menu-button-style {:disabled? true})]
           (str "<div style=\""menu-button-style"\">..</div>"))))

(defn- home-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [path]}]
  (if (vector/nonempty? path)
      (let [menu-button-style (re-frame-browser.styles/menu-button-style)]
           (str "<div><a style=\""menu-button-style"\" href=\"?path=[]\">/</a></div>"))
      (let [menu-button-style (re-frame-browser.styles/menu-button-style {:disabled? true})]
           (str "<div style=\""menu-button-style"\">/</div>"))))

(defn- menu-bar
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [browser-props]
  (let [menu-bar-style (re-frame-browser.styles/menu-bar-style)]
       (str "<div style=\""menu-bar-style"\">"
            (up-button   browser-props)
            (home-button browser-props)
            "</div>")))

(defn- map-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [path]} k v]
  (let [target-path    (conj path k)
        map-item-style (re-frame-browser.styles/map-item-style)]
       (str "<div><a style=\""map-item-style"\" href=\"?path=" target-path "\">" k "</a></div>")))

(defn- map-items
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [current-item] :as browser-props}]
  (letfn [(f [o k] (str o (map-item browser-props k (k current-item))))]
         (let [keys (-> current-item map/get-keys vector/abc-items)]
              (reduce f "" keys))))

(defn- re-frame-browser
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [current-item] :as browser-props}]
  (str (menu-bar browser-props)
       (cond (map?    current-item) (map-items browser-props)
             (nil?    current-item) (str "<div style=\"padding: 8px\">nil</div>")
             (string? current-item) (str "<div style=\"padding: 8px\">\"" current-item "\"</div>")
             :else                  (str "<div style=\"padding: 8px\">"   current-item "</div>"))))

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [query-params]}]
  (let [db          @(a/subscribe [:db/get-db])
        path         (reader/string->mixed (get query-params "path" "[]"))
        current-item (get-in db path)]
       (re-frame-browser {:current-item current-item :path path})))