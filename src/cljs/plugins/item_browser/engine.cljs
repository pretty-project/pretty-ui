
(ns plugins.item-browser.engine
    (:require [x.app-core.api :as a :refer [r]]))



;; -- Item-browser subscriptions ----------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-current-path
  ; @param (string) extension-name
  ;
  ; @usage
  ;  (r item-browser/get-current-path db "media")
  ;
  ; @return (vector)
  [db [_ extension-name]]
  (let [extension-id (keyword extension-name)
        current-path (get-in db [extension-id :browser-meta :current-path])]
       (vec current-path)))

(defn at-home?
  ; @param (string) extension-name
  ;
  ; @usage
  ;  (r item-browser/at-home? db "media")
  ;
  ; @return (boolean)
  [db [_ extension-name]]
  (let [current-path (r get-current-path db extension-name)]
       (empty? current-path)))

(defn get-header-view-props
  ; @param (string) extension-name
  ;
  ; @usage
  ;  (r item-browser/get-header-view-props db "media")
  ;
  ; @return (map)
  ;  {:at-home? (boolean)}
  [db [_ extension-name]]
  {:at-home? (r at-home? db extension-name)})



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------



; WARNING!

; subbrowser-ként fog menni a file-tallózó
;
; Re-Frame DB:
; {:media {:browser-data [] (ez közös)
;          :browser-meta {}
;          :subbrowser-meta {}}}

; WARNING!



(a/reg-event-fx
  :item-browser/load!
  ; @param (string) extension-name
  ; @param (string) item-name
  ; @param (map) browser-props
  ;  {:default-item-id (string)(opt)}
  ;
  ; @usage
  ;  [:item-browser/load! "media" "directory" {:default-item-id "home"}]
  (fn [{:keys [db]} [_ extension-name item-name {:keys [default-item-id]}]]
      (let [render-event (keyword extension-name (str "render-" item-name "-browser!"))
            extension-id (keyword extension-name)
            item-id-key  (keyword/join item-name "-id")
            item-id      (or (r router/get-current-route-path-param db item-id-key)
                             (param default-item-id))]
                              ;(assoc-in  [:media :browser-meta :directory-id] "my-directory")
           {:db         (-> db (assoc-in  [extension-id :browser-meta item-id-key] item-id)
                              ;(dissoc-in [:meda :browser-data])
                               (dissoc-in [extension-id :browser-data]))
            :dispatch-n [[:x.app-ui/listen-to-process! :extensions/request-browser-items!]
                        ;[:x.app-ui/set-header-title!  :media]
                         [:x.app-ui/set-header-title!  extension-id]
                        ;[:x.app-ui/set-window-title!  :media]
                         [:x.app-ui/set-window-title!  extension-id]
                        ;[:media/render-directory-browser!]
                         [render-event]]})))

(a/reg-event-fx
  :item-browser/add-routes!
  ; @param (string) extension-name
  ; @param (string) item-name
  ; @param (map) browser-props
  ;
  ; @usage
  ;  [:item-browser/add-routes! "media" "directory"]
  (fn [_ [_ extension-name item-name browser-props]]
           ;route-id          :media/route
      (let [route-id          (keyword extension-name "route")
           ;extended-route-id :media/extended-route
            extended-route-id (keyword extension-name "extended-route")]
           {:dispatch-n [[:x.app-router/add-route! route-id
                                                   {:route-event    [:item-browser/load! extension-name item-name browser-props]
                                                   ;:route-template "/media"
                                                    :route-template (str "/" extension-name)
                                                    :restricted?    true}]
                         [:x.app-router/add-route! extended-route-id
                                                   {:route-event    [:item-browser/load! extension-name item-name browser-props]
                                                   ;:route-template "/media/:directory-id"
                                                    :route-template (str "/" extension-name "/:" item-name "-id")
                                                    :restricted?    true}]]})))
