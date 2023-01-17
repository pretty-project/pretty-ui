
(ns templates.item-handler.warnings.views
    (:require [re-frame.api     :as r]
              [x.components.api :as x.components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- item-changed-warning
  ; @param (keyword) handler-id
  ; @param (map) warnings-props
  [handler-id _]
  (if-let [current-item-changed? @(r/subscribe [:item-handler/current-item-changed? handler-id])]
          [:div {:class            :t-item-handler--warning
                 :data-font-size   :xs
                 :data-font-weight :medium
                 :data-line-height :xs}
                (x.components/content "Az elem el nem mentett változtatásokat tartalmaz")]

          ; Placeholder: to prevent the content from jumping down when the user first
          ; changes the item and the item-changed-warning component steps into active state
          [:div {:class :t-item-handler--warning-placeholder}]))

(defn view
  ; @param (keyword) handler-id
  ; @param (map) warnings-props
  ; {}
  ;
  ; @usage
  ; [warnings :my-handler {...}]
  [handler-id warnings-props]
  (let []; warnings-props (warnings.prototypes/warnings-props-prototype handler-id warnings-props)
       [:div {:id :t-item-handler--warnings}
             [:div {:id :t-item-handler--warnings-body}
                   [item-changed-warning handler-id warnings-props]]]))
