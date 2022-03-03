
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-plugins.item-editor.effects
    (:require [mid-fruits.candy                  :refer [param return]]
              [server-plugins.item-editor.engine :as engine]
              [server-plugins.item-editor.events :as events]
              [x.server-core.api                 :as a :refer [r]]))



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
  ;  {:collection-name (string)
  ;   :routed? (boolean)}
  [extension-id _ editor-props]
  (merge {:collection-name (name extension-id)
          :routed? true}
         (param editor-props)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-editor/init-editor!
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map)(opt) editor-props
  ;  {:collection-name (string)(opt)
  ;    Default: (name extension-id)
  ;   :routed? (boolean)(opt)
  ;    Default: true
  ;   :suggestion-keys (keywords in vector)(opt)}
  ;
  ; @usage
  ;  [:item-editor/init-editor! :my-extension :my-type]
  ;
  ; @usage
  ;  [:item-editor/init-editor! :my-extension :my-type {...}]
  ;
  ; @usage
  ;  [:item-editor/init-editor! :my-extension :my-type {:suggestion-keys [:color :city ...]}]
  (fn [{:keys [db]} [_ extension-id item-namespace editor-props]]
      (let [editor-props (editor-props-prototype extension-id item-namespace editor-props)]
           {:db (r events/init-editor! db extension-id item-namespace editor-props)
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
