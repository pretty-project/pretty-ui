
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
              [x.app-router.api   :as router]
              [mid-plugins.item-browser.engine :as engine]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid-plugins.item-browser.engine
(def request-id              engine/request-id)
(def route-id                engine/route-id)
(def extended-route-id       engine/extended-route-id)
(def route-template          engine/route-template)
(def extended-route-template engine/extended-route-template)
(def go-up-event             engine/go-up-event)
(def go-home-event           engine/go-home-event)
(def render-event            engine/render-event)



;; -- Item-browser subscriptions ----------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-derived-item-id
  ; @param (keyword) extension-id
  ; @param (map) selector-props
  ;  {:default-item-id (keyword)(opt)}
  ;
  ; @usage
  ;  (r item-browser/get-derived-item-id db :my-extension {...})
  ;
  ; @return (keyword)
  ;  Az item-id forrásából (route-path param) származó adat. Annak hiánya esetén a default-item-id
  [db [_ extension-id {:keys [default-item-id]}]]
  (if-let [derived-item-id (r router/get-current-route-path-param db :item-id)]
          (let [derived-item-id (keyword derived-item-id)]
               (return derived-item-id))
          (return default-item-id)))

; @usage
;  [:item-browser/get-derived-item-id :my-extension {...}]
(a/reg-sub :item-browser/get-derived-item-id get-derived-item-id)

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

; @usage
;  [:item-browser/get-current-path :my-extension]
(a/reg-sub :item-browser/get-current-path get-current-path)

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

; @usage
;  [:item-browser/at-home? :my-extension]
(a/reg-sub :item-browser/at-home? at-home?)

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

; @usage
;  [:item-browser/get-header-props :my-extension]
(a/reg-sub :item-browser/get-header-props get-header-props)



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
      (let [derived-item-id (r get-derived-item-id db extension-id browser-props)]
           {:db         (-> db (dissoc-in [extension-id :browser-data])
                               (dissoc-in [extension-id :browser-meta])
                               (assoc-in  [extension-id :browser-meta :item-id] derived-item-id)

                              ; Ha az item-browser tényleg az item-listerre épül, akkor az
                              ; item-lister infinite-loader komponense által indított request
                              ; a :lister-data és :lister-meta db elemeket tölti fel, szóval
                              ; azokat is nullázni kell itt, amikor betölt a browser,
                              ; mert az item-lister is nullázza őket magának.
                              ; Pl.: Ha 0 elem van a kollekcioban, akkor az újabb load eseménynél
                              ; nem próbálná meg újra megnézni a szerón, hogy vannak-e elemek
                              ; mert emlékezne, hogy utoljára nulla volt, stb ...
                               (dissoc-in [extension-id :lister-meta]))

            :dispatch-n [[:ui/listen-to-process! (request-id extension-id item-namespace)]
                         [:ui/set-header-title!  (param      extension-id)]
                         [:ui/set-window-title!  (param      extension-id)]
                         (render-event extension-id item-namespace)]})))
