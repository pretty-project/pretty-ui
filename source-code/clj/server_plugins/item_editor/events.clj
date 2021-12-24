
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.12.18
; Description:
; Version: v0.4.0
; Compatibility: x4.4.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-plugins.item-editor.events
    (:require [mid-fruits.candy  :refer [param return]]
              [x.server-core.api :as a :refer [r]]
              [server-plugins.item-editor.engine :as engine]))



;; -- Names -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @name multi-view?
;  TODO ...
;
; @name suggestion-keys
;  Az :item-editor/initialize! esemény számára {:suggestion-keys [...]} tulajdonságként
;  átadott kulcsokhoz tartozó értékeket az item-editor a kliens-oldali betöltődésekor
;  letölti a szerveren tárolt dokumentumokból, így azok a kliens-oldalon elérhetővé válnak
;  a dokumentum szerkesztéséhez használt mezők számára.



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- editor-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map)(opt) editor-props
  ;
  ; @return (map)
  ;  {:handle-archived-items? (boolean)
  ;   :handle-favorite-items? (boolean)}
  [editor-props]
  (merge {:handle-archived-items? true
          :handle-favorite-items? true}
         (param editor-props)))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- add-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map)(opt) editor-props
  [_ [_ extension-id item-namespace editor-props]]
  [:router/add-route! (engine/route-id extension-id item-namespace)
                      {:route-template (engine/route-template        extension-id)
                       :route-parent   (engine/parent-uri            extension-id)
                       :client-event   [:item-editor/load-editor!    extension-id item-namespace editor-props]
                       :on-leave-event [:item-editor/->editor-leaved extension-id item-namespace]
                       :restricted?    true}])

(defn- add-extended-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map)(opt) editor-props
  [_ [_ extension-id item-namespace editor-props]]
  [:router/add-route! (engine/extended-route-id extension-id item-namespace)
                      {:route-template (engine/extended-route-template extension-id)
                       :route-parent   (engine/parent-uri              extension-id)
                       :client-event   [:item-editor/load-editor!      extension-id item-namespace editor-props]
                       :on-leave-event [:item-editor/->editor-leaved   extension-id item-namespace]
                       :restricted?    true}])

(a/reg-event-fx
  :item-editor/initialize!
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map)(opt) editor-props
  ;  {:handle-archived-items? (boolean)(opt)
  ;    Default: true
  ;   :handle-favorite-items? (boolean)(opt)
  ;    Default: true
  ;   :multi-view? (boolean)(opt)
  ;    Default: false
  ;   :suggestion-keys (keywords in vector)(opt)}
  ;
  ; @usage
  ;  [:item-editor/initialize! :my-extension :my-type]
  ;
  ; @usage
  ;  [:item-editor/initialize! :my-extension :my-type {...}]
  ;
  ; @usage
  ;  [:item-editor/initialize! :my-extension :my-type {:suggestion-keys [:color :city ...]}]
  (fn [cofx [_ extension-id item-namespace editor-props]]
      (let [editor-props (editor-props-prototype editor-props)]
           (if-let [multi-view? (get editor-props :multi-view?)]
                   {:dispatch-n [(r add-route!          cofx extension-id item-namespace editor-props)
                                 (r add-extended-route! cofx extension-id item-namespace editor-props)]}
                   {:dispatch    (r add-route!          cofx extension-id item-namespace editor-props)}))))
