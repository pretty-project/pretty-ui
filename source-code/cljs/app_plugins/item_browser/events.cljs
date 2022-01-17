
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.21
; Description:
; Version: v0.3.0
; Compatibility: x4.5.0



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-browser.events
    (:require [mid-fruits.candy :refer [param return]]
              [x.app-core.api   :as a :refer [r]]
              [app-plugins.item-browser.engine :as engine]
              [app-plugins.item-browser.subs   :as subs]))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn store-current-item-id!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) browser-props
  ;
  ; @return (map)
  [db [_ extension-id item-namespace browser-props]]
  (let [derived-item-id (r subs/get-derived-item-id db extension-id item-namespace browser-props)]
       (assoc-in db [extension-id :item-browser/meta-items :item-id] derived-item-id)))

(defn load-browser!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) browser-props
  ;
  ; @return (map)
  [db [_ extension-id item-namespace browser-props]]
  (as-> db % (r store-current-item-id! % extension-id item-namespace browser-props)

             ; TEMP
             (r app-plugins.item-lister.events/load-lister! % extension-id item-namespace browser-props)))




; WARNING!

; subbrowser-ként fog menni a file-tallózó
;
; Re-Frame DB:
; {:media {:browser-data [] (ez közös)
;          :browser-meta {}
;          :subbrowser-meta {}}}

; WARNING!

(a/reg-event-fx
  :item-browser/browse-item!
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @usage
  ;  [:item-browser/browse-item! :my-extension :my-type "my-item"]
  (fn [_ [_ extension-id item-namespace item-id]]
      (let [browser-uri (engine/browser-uri extension-id item-namespace item-id)]
           [:router/go-to! browser-uri])))

(a/reg-event-fx
  :item-browser/load-browser!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) browser-props
  ;  {:default-item-id (string)(opt)}
  ;
  ; @usage
  ;  [:item-browser/load! :my-extension :my-type {:default-item-id "my-item"}]
  (fn [{:keys [db]} [_ extension-id item-namespace browser-props]]
      (let []
           {:db  (r load-browser! db extension-id item-namespace browser-props)
                      ;  (-> db (dissoc-in [extension-id :browser-data])
                      ;         (dissoc-in [extension-id :browser-meta])
                      ;         (assoc-in  [extension-id :browser-meta :item-id] derived-item-id)

                              ; Ha az item-browser tényleg az item-listerre épül, akkor az
                              ; item-lister infinite-loader komponense által indított request
                              ; a :lister-data és :lister-meta db elemeket tölti fel, szóval
                              ; azokat is nullázni kell itt, amikor betölt a browser,
                              ; mert az item-lister is nullázza őket magának.
                              ; Pl.: Ha 0 elem van a kollekcioban, akkor az újabb load eseménynél
                              ; nem próbálná meg újra megnézni a szerón, hogy vannak-e elemek
                              ; mert emlékezne, hogy utoljára nulla volt, stb ...
                      ;         (dissoc-in [extension-id :lister-meta])



            :dispatch-n [[:ui/set-header-title!  (param extension-id)]
                         [:ui/set-window-title!  (param extension-id)]
                         (engine/load-extension-event extension-id item-namespace)

                         ; Nem tudom itt-e a helye.
                         ; Amikor böngészel és a viewport-ban volt az inf-loader, és megnyitsz
                         ; egy elemet, akkor nem kerül ki és vissza a viewportba a loader, ezért
                         ; manuál kell ujrainditani
                         [:tools/reload-infinite-loader! extension-id]]})))
