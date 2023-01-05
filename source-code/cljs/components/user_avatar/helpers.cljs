
(ns components.user-avatar.helpers
    (:require [components.component.helpers :as component.helpers]
              [css.api                      :as css]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn avatar-body-attributes
  ; @param (keyword) avatar-id
  ; @param (map) avatar-props
  ; {:size (px)
  ;  :style (map)(opt)}
  ;
  ; @return (map)
  ; {:style (map)}
  [avatar-id {:keys [size style] :as avatar-props}]
  (merge (component.helpers/component-indent-attributes avatar-id avatar-props)
         {:style (merge style {:height (css/px size)
                               :width  (css/px size)})}))

(defn avatar-attributes
  ; @param (keyword) avatar-id
  ; @param (map) avatar-props
  ;
  ; @return (map)
  [avatar-id avatar-props]
  (merge (component.helpers/component-default-attributes avatar-id avatar-props)
         (component.helpers/component-outdent-attributes avatar-id avatar-props)))
