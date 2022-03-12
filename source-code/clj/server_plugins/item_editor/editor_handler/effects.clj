
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-plugins.item-editor.editor-handler.effects
    (:require [mid-fruits.candy                                :refer [param return]]
              [server-plugins.item-editor.route-handler.engine :as route-handler.engine]
              [x.server-core.api                               :as a]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn editor-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) editor-props
  ;
  ; @return (map)
  ;  {:base-route (string)
  ;   :extended-route (string)
  ;   :route-title (keyword)}
  [extension-id item-namespace editor-props]
  (merge {:base-route     (route-handler.engine/base-route     extension-id item-namespace editor-props)
          :extended-route (route-handler.engine/extended-route extension-id item-namespace editor-props)
          :route-title    (param :auto)}
         (param editor-props)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-editor/init-editor!
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) editor-props
  ;  {:on-load (metamorphic-event)
  ;   :route-template (string)
  ;    Az útvonalnak az ".../:item-id" kifejezésre kell végződnie!
  ;   :route-title (keyword or metamorphic-content)(opt) :auto}
  ;
  ; @usage
  ;  [:item-editor/init-editor! :my-extension :my-type {...}]
  (fn [{:keys [db]} [_ extension-id item-namespace editor-props]]
      (let [editor-props (editor-props-prototype extension-id item-namespace editor-props)]
           {:dispatch-n [[:item-editor/reg-transfer-editor-props! extension-id item-namespace editor-props]
                         [:item-editor/add-route!                 extension-id item-namespace editor-props]]})))
