
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-plugins.item-editor.effects
    (:require [mid-fruits.candy                  :refer [param return]]
              [mid-fruits.uri                    :as uri]
              [server-plugins.item-editor.engine :as engine]
              [x.server-core.api                 :as a :refer [r]]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn editor-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) editor-props
  ;  {:base-route (string)}
  ;
  ; @return (map)
  ;  {:base-route (string)
  ;   :route-title (keyword)}
  [extension-id item-namespace {:keys [base-route] :as editor-props}]
  (merge {:base-route (uri/valid-path base-route)
          :route-title :auto}
         (param editor-props)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-editor/init-editor!
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) editor-props
  ;  {:base-route (string)
  ;   :on-load (metamorphic-event)
  ;   :route-title (keyword or metamorphic-content)(opt) :auto}
  ;
  ; @usage
  ;  [:item-editor/init-editor! :my-extension :my-type {...}]
  (fn [{:keys [db]} [_ extension-id item-namespace editor-props]]
      (let [editor-props (editor-props-prototype extension-id item-namespace editor-props)]
           {:dispatch-n [[:item-editor/reg-transfer-editor-props! extension-id item-namespace editor-props]
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
  (fn [_ [_ extension-id item-namespace editor-props]]
      [:router/add-route! (engine/route-id extension-id item-namespace)
                          {:route-template (engine/route-template     extension-id item-namespace editor-props)
                           :client-event   [:item-editor/load-editor! extension-id item-namespace]
                           :restricted?    true}]))
