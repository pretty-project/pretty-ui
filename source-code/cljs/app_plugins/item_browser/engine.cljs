
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.21
; Description:
; Version: v0.3.2
; Compatibility: x4.4.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-browser.engine
    (:require [mid-fruits.candy   :refer [param return]]
              [mid-fruits.keyword :as keyword]
              [mid-fruits.map     :refer [dissoc-in]]
              [x.app-core.api     :as a :refer [r]]
              [x.app-router.api   :as router]))



;; -- Item-browser subscriptions ----------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-current-path
  ; @param (keyword) extension-id
  ;
  ; @usage
  ;  (r item-browser/get-current-path db :my-extension)
  ;
  ; @return (vector)
  [db [_ extension-id]]
  (let [current-path (get-in db [extension-id :browser-meta :current-path])]
       (vec current-path)))

(defn at-home?
  ; @param (keyword) extension-id
  ;
  ; @usage
  ;  (r item-browser/at-home? db :my-extension)
  ;
  ; @return (boolean)
  [db [_ extension-id]]
  (let [current-path (r get-current-path db extension-id)]
       (empty? current-path)))

(defn get-header-props
  ; @param (keyword) extension-id
  ;
  ; @usage
  ;  (r item-browser/get-header-props db :my-extension)
  ;
  ; @return (map)
  ;  {:at-home? (boolean)}
  [db [_ extension-id]]
  {:at-home? (r at-home? db extension-id)})



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
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) browser-props
  ;  {:default-item-id (keyword)(opt)}
  ;
  ; @usage
  ;  [:item-browser/load! :my-extension :my-type {:default-item-id :my-item}]
  (fn [{:keys [db]} [_ extension-id item-namespace browser-props]]
      (let [derived-item (r get-derived-item db extension-id browser-props)]
           {:db         (-> db (dissoc-in [extension-id :browser-data])
                               (dissoc-in [extension-id :browser-meta])
                               (assoc-in  [extension-id :browser-meta :item-id] derived-item)

                              ; Ha az item-browser tényleg az item-listerre épül, akkor az
                              ; item-lister infinite-loader komponense által indított request
                              ; a :lister-data és :lister-meta db elemeket tölti fel, szóval
                              ; azokat is nullázni kell itt, amikor betölt a browser,
                              ; mert az item-lister is nullázza őket magának.
                              ; Pl.: Ha 0 elem van a kollekcioban, akkor az újabb load eseménynél
                              ; nem próbálná meg újra megnézni a szerón, hogy vannak-e elemek
                              ; mert emlékezne, hogy utoljára nulla volt, stb ...
                               (dissoc-in [extension-id :lister-meta]))

            :dispatch-n [[:x.app-ui/listen-to-process! (request-id extension-id item-namespace)]
                         [:x.app-ui/set-header-title!  (param      extension-id)]
                         [:x.app-ui/set-window-title!  (param      extension-id)]
                         (render-event extension-id item-namespace)]})))
