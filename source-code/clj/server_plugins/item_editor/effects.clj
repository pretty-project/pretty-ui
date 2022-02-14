
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.12.18
; Description:
; Version: v0.7.0
; Compatibility: x4.6.0



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-plugins.item-editor.effects
    (:require [mid-fruits.candy  :refer [param return]]
              [x.server-core.api :as a :refer [r]]
              [server-plugins.item-editor.engine :as engine]
              [server-plugins.item-editor.events :as events]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn editor-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map)(opt) editor-props
  ;
  ; @return (map)
  ;  {:routed? (boolean)}
  [_ _ editor-props]
  (merge {:routed? true}
         (param editor-props)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-editor/initialize-editor!
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map)(opt) editor-props
  ;  {:label (metamorphic-content)(opt)
  ;   :routed? (boolean)(opt)
  ;    Default: true
  ;   :suggestion-keys (keywords in vector)(opt)}
  ;
  ; @usage
  ;  [:item-editor/initialize-editor! :my-extension :my-type]
  ;
  ; @usage
  ;  [:item-editor/initialize-editor! :my-extension :my-type {...}]
  ;
  ; @usage
  ;  [:item-editor/initialize-editor! :my-extension :my-type {:suggestion-keys [:color :city ...]}]
  (fn [{:keys [db]} [_ extension-id item-namespace editor-props]]
      (let [editor-props (editor-props-prototype extension-id item-namespace editor-props)]
           {:db (r events/initialize-editor! db extension-id item-namespace editor-props)
            :dispatch-n [[:item-editor/reg-transfer-editor-props! extension-id item-namespace editor-props]
                         [:item-editor/add-route!                 extension-id item-namespace editor-props]]})))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-editor/reg-transfer-editor-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) editor-props
  (fn [_ [_ extension-id item-namespace editor-props]]
      {:fx [:core/reg-transfer! (engine/transfer-id extension-id item-namespace)
                                {:data-f      (fn [_] (return editor-props))
                                 :target-path [extension-id :item-editor/meta-items]}]}))

(a/reg-event-fx
  :item-editor/add-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) editor-props
  ;  {:routed? (boolean)}
  (fn [_ [_ extension-id item-namespace {:keys [routed?]}]]
      (if routed? [:router/add-route! (engine/route-id extension-id item-namespace)
                                      {:route-template (engine/route-template        extension-id item-namespace)
                                       :route-parent   (engine/parent-uri            extension-id item-namespace)
                                       :client-event   [:item-editor/load-editor!    extension-id item-namespace]
                                       :restricted?    true}])))
