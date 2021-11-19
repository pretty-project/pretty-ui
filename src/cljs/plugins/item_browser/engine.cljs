
(ns plugins.item-browser.engine
    (:require [mid-fruits.candy   :refer [param return]]
              [mid-fruits.keyword :as keyword]
              [mid-fruits.map     :refer [dissoc-in]]
              [x.app-core.api     :as a :refer [r]]
              [x.app-router.api   :as router]))



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

(defn get-header-props
  ; @param (string) extension-name
  ;
  ; @usage
  ;  (r item-browser/get-header-props db "media")
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
                              ;(dissoc-in [:media :browser-data])
           {:db         (-> db (dissoc-in [extension-id :browser-data])
                              ;(dissoc-in [:media :browser-meta])
                               (dissoc-in [extension-id :browser-meta])
                              ;(assoc-in  [:media :browser-meta :directory-id] "my-directory")
                               (assoc-in  [extension-id :browser-meta item-id-key] item-id)

                              ; Ha az item-browser tényleg az item-listerre épül, akkor az
                              ; item-lister infinite-loader komponense által indított request
                              ; a :lister-data és :lister-meta db elemeket tölti fel, szóval
                              ; azokat is nullázni kéne itt, amikor betölt a browser,
                              ; mert az item-lister is nullázza őket magának.
                              ; Pl.: Ha 0 elem van a kollekcioban, akkor az újabb load eseménynél
                              ; nem próbálná meg újra megnézni a szerón, hogy vannak-e elemek
                              ; mert emlékezne, hogy utoljára nulla volt, és ilyenek, stb.
                               (dissoc-in [extension-id :lister-meta]))

            :dispatch-n [[:x.app-ui/listen-to-process! :item-lister/synchronize!]
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
  ;  [:item-browser/add-routes! "media" "directory" {...}]
  (fn [_ [_ extension-name item-name browser-props]]
      (let [route-id          (keyword extension-name "route")
            extended-route-id (keyword extension-name "extended-route")]
                        ;[:x.app-router/add-route! :media/route
           {:dispatch-n [[:x.app-router/add-route! route-id
                                                   {:route-event    [:item-browser/load! extension-name item-name browser-props]
                                                   ;:route-template "/media"
                                                    :route-template (str "/" extension-name)
                                                    :restricted?    true}]
                        ;[:x.app-router/add-route! :media/extended-route
                         [:x.app-router/add-route! extended-route-id
                                                   {:route-event    [:item-browser/load! extension-name item-name browser-props]
                                                   ;:route-template "/media/:directory-id"
                                                    :route-template (str "/" extension-name "/:" item-name "-id")
                                                    :restricted?    true}]]})))
