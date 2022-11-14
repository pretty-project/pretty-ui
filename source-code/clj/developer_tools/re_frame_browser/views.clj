
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns developer-tools.re-frame-browser.views
    (:require [developer-tools.re-frame-browser.styles :as re-frame-browser.styles]
              [mid-fruits.map                          :as map]
              [mid-fruits.pretty                       :as pretty]
              [reader.api                              :as reader]
              [mid-fruits.vector                       :as vector]
              [re-frame.api                            :as r]
              [server-fruits.http                      :as http]))



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
  (let [item-preview-style (re-frame-browser.styles/item-preview-style)]
       (str (menu-bar browser-props)
            "<div style=\"display: flex\">"
            "<div style=\"width: 50%\">"
            (cond (map?    current-item) (map-items browser-props)
                  (nil?    current-item) (str "<div style=\"padding: 8px\">nil</div>")
                  (string? current-item) (str "<div style=\"padding: 8px\">\"" current-item "\"</div>")
                  :return                (str "<div style=\"padding: 8px\">"   current-item "</div>"))
            "</div>"
            "<div style\"width: 50%;\">"
            "<pre style=\""item-preview-style"\">"(pretty/mixed->string current-item {:abc? true})"</pre>"
            "</div>"
            "</div>")))

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [query-params]}]
  (let [db          @(r/subscribe [:x.db/get-db])
        path         (reader/string->mixed (get query-params "path" "[]"))
        current-item (get-in db path)]
       (re-frame-browser {:current-item current-item :path path})))
