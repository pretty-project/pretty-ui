
(ns components.action-bar.views
    (:require [components.action-bar.prototypes :as action-bar.prototypes]
              [elements.api                     :as elements]
              [random.api                       :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- action-button
  ; @param (keyword) bar-id
  ; @param (map) bar-props
  ; {:disabled? (boolean)(opt)
  ;  :label (metamorphic-content)
  ;  :on-click (metamorphic-event)}
  [_ {:keys [disabled? label on-click]}]
  [elements/button {:color     :primary
                    :disabled? disabled?
                    :font-size :xs
                    :label     label
                    :on-click  on-click}])

(defn- action-bar
  ; @param (keyword) bar-id
  ; @param (map) bar-props
  ; {:indent (map)(opt)}
  [bar-id {:keys [indent] :as bar-props}]
  [elements/row bar-id
                {:content          [action-button bar-id bar-props]
                 :horizontal-align :center
                 :indent           indent}])

(defn component
  ; @param (keyword)(opt) bar-id
  ; @param (map) bar-props
  ; {:disabled? (boolean)(opt)}
  ;   Default: false
  ;  :font-size (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;   Default: :s
  ;  :indent (map)(opt)
  ;  :label (metamorphic-content)
  ;  :outdent (map)(opt)
  ;  :placeholder (metamorphic-content)(opt)
  ;  :value (metamorphic-content)(opt)}
  ;
  ; @usage
  ; [action-bar {...}]
  ;
  ; @usage
  ; [action-bar :my-action-bar {...}]
  ([bar-props]
   [component (random/generate-keyword) bar-props])

  ([bar-id bar-props]
   (let [] ; bar-props (action-bar.prototypes/bar-props-prototype bar-props)
        [action-bar bar-id bar-props])))
