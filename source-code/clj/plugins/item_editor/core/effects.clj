
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.core.effects
    (:require [plugins.item-editor.core.prototypes :as core.prototypes]
              [x.server-core.api                   :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-editor/init-editor!
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) editor-props
  ;  {:on-route (metamorphic-event)(opt)
  ;   :route-template (string)(opt)
  ;    Az útvonalnak az ".../:item-id" kifejezésre kell végződnie!
  ;   :route-title (metamorphic-content)(opt)}
  ;
  ; @usage
  ;  [:item-editor/init-editor! :my-extension :my-type {...}]
  (fn [{:keys [db]} [_ extension-id item-namespace {:keys [route-template] :as editor-props}]]
      (let [editor-props (core.prototypes/editor-props-prototype extension-id item-namespace editor-props)]
           {:dispatch-n [[:item-editor/reg-transfer-editor-props! extension-id item-namespace editor-props]
                         (if route-template [:item-editor/add-route! extension-id item-namespace editor-props])]})))
