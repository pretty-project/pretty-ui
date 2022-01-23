
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.12.18
; Description:
; Version: v0.5.6
; Compatibility: x4.5.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-plugins.item-editor.events
    (:require [mid-fruits.candy  :refer [param return]]
              [x.server-core.api :as a :refer [r]]
              [mid-plugins.item-editor.events    :as events]
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



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid-plugins.item-editor.events
(def store-editor-props! events/store-editor-props!)



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



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn initialize!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map)(opt) editor-props
  ;
  ; @return (map)
  [db [_ extension-id item-namespace editor-props]]
  (r store-editor-props! db extension-id item-namespace editor-props))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn transfer-editor-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) editor-props
  [_ [_ extension-id item-namespace editor-props]]
  [:core/reg-transfer! {:data-f      (fn [_] (return editor-props))
                        :target-path [extension-id :item-editor/meta-items]}])

(defn add-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) editor-props
  ;  {:routed? (boolean)}
  [_ [_ extension-id item-namespace {:keys [routed?]}]]
  (if routed? [:router/add-route! (engine/route-id extension-id item-namespace)
                                  {:route-template (engine/route-template        extension-id)
                                   :route-parent   (engine/parent-uri            extension-id)
                                   :client-event   [:item-editor/load-editor!    extension-id item-namespace]
                                   :on-leave-event [:item-editor/->editor-leaved extension-id item-namespace]
                                   :restricted?    true}]))

(a/reg-event-fx
  :item-editor/initialize!
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map)(opt) editor-props
  ;  {:label (metamorphic-content)(opt)
  ;   :routed? (boolean)(opt)
  ;    Default: true
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
  (fn [{:keys [db] :as cofx} [_ extension-id item-namespace editor-props]]
      (let [editor-props (editor-props-prototype extension-id item-namespace editor-props)]
           {:db (r initialize! db extension-id item-namespace editor-props)
            :dispatch-n [(r transfer-editor-props! cofx extension-id item-namespace editor-props)
                         (r add-route!             cofx extension-id item-namespace editor-props)]})))
