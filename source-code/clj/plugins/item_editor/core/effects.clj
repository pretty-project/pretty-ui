
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.core.effects
    (:require [plugins.item-editor.core.events     :as core.events]
              [plugins.item-editor.core.prototypes :as core.prototypes]
              [x.server-core.api                   :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-editor/init-editor!
  ; @param (keyword) editor-id
  ; @param (map) editor-props
  ;  {:base-route (string)(opt)
  ;   :collection-name (string)
  ;   :handler-key (keyword)
  ;   :item-namespace (keyword)
  ;   :on-route (metamorphic-event)(opt)
  ;   :route-title (metamorphic-content)(opt)}
  ;
  ; @usage
  ;  [:item-editor/init-editor! :my-editor {...}]
  (fn [{:keys [db]} [_ editor-id {:keys [base-route] :as editor-props}]]
      (let [editor-props (core.prototypes/editor-props-prototype editor-id editor-props)]
           {:db         (r core.events/init-editor! db editor-id editor-props)
            :dispatch-n [[:item-editor/reg-transfer-editor-props! editor-id editor-props]
                         (if base-route [:item-editor/add-extended-route! editor-id editor-props])
                         (if base-route [:item-editor/add-sub-route!      editor-id editor-props])]})))
