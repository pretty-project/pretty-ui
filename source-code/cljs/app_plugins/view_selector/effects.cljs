
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.21
; Description:
; Version: v0.7.6
; Compatibility: x4.6.0



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.view-selector.effects
    (:require [x.app-core.api :as a :refer [r]]
              [app-plugins.view-selector.engine :as engine]
              [app-plugins.view-selector.events :as events]
              [app-plugins.view-selector.subs   :as subs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :view-selector/load-selector!
  ; @param (keyword) extension-id
  ; @param (map)(opt) selector-props
  ;  {:view-id (keyword)(opt)}
  ;
  ; @usage
  ;  [:view-selector/load-selector! :my-extension]
  ;
  ; @usage
  ;  [:view-selector/load-selector! :my-extension {...}]
  ;
  ; @usage
  ;  [:view-selector/load-selector! :my-extension {:view-id "my-view"}]
  (fn [{:keys [db]} [_ extension-id selector-props]]
      (let [route-title (r subs/get-meta-item db extension-id :route-title)]
           {:db (r events/load-selector! db extension-id selector-props)
            :dispatch-n [; XXX#3237
                         (if (r subs/set-title? db extension-id)
                             [:ui/set-title! route-title])
                         (engine/load-extension-event extension-id)]})))

(a/reg-event-fx
  :view-selector/go-to!
  ; @param (keyword) extension-id
  ; @param (keyword) view-id
  ;
  ; @usage
  ;  [:view-selector/go-to! :my-extension :my-view]
  (fn [{:keys [db]} [_ extension-id view-id]]
      (if (r subs/route-handled? db extension-id)
          (let [target-route-string (engine/extended-route-string extension-id view-id)]
               [:router/go-to! target-route-string])
          [:view-selector/load-selector! extension-id {:view-id view-id}])))
