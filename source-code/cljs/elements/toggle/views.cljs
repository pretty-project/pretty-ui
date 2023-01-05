
(ns elements.toggle.views
    (:require [elements.toggle.helpers    :as toggle.helpers]
              [elements.toggle.prototypes :as toggle.prototypes]
              [random.api                 :as random]
              [x.components.api           :as x.components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- toggle-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) toggle-id
  ; @param (map) toggle-props
  ; {:content (metamorphic-content)(opt)}
  [toggle-id {:keys [content] :as toggle-props}]
  [:button.e-toggle--body (toggle.helpers/toggle-body-attributes toggle-id toggle-props)
                          [x.components/content                  toggle-id content]])

(defn- toggle
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) toggle-id
  ; @param (map) toggle-props
  [toggle-id toggle-props]
  [:div.e-toggle (toggle.helpers/toggle-attributes toggle-id toggle-props)
                 [toggle-body                      toggle-id toggle-props]])

(defn element
  ; @param (keyword)(opt) toggle-id
  ; @param (map) toggle-props
  ; {:background-color (keyword)(opt)
  ;   :highlight, :muted, :none, :primary, :secondary, :success, :warning
  ;   Default: :none
  ;  :border-radius (keyword)(opt)
  ;   :none, :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;   Default: :none
  ;  :class (keyword or keywords in vector)(opt)
  ;  :content (metamorphic-content)
  ;  :disabled? (boolean)(opt)
  ;   Default: false
  ;  :hover-color (keyword or string)(opt)
  ;   :highlight, :invert, :muted, :none, :primary, :secondary, :success, :warning
  ;   Default: :none
  ;  :indent (map)(opt)
  ;   {:bottom (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    :left (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    :right (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    :top (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl}
  ;  :on-click (metamorphic-event)
  ;  :on-right-click (metamorphic-event)(opt)
  ;  :outdent (map)(opt)
  ;  :style (map)(opt)}
  ;
  ; @usage
  ; [toggle {...}]
  ;
  ; @usage
  ; [toggle :my-toggle {...}]
  ([toggle-props]
   [element (random/generate-keyword) toggle-props])

  ([toggle-id toggle-props]
   (let [];toggle-props (toggle.prototypes/toggle-props-prototype toggle-props)
        [toggle toggle-id toggle-props])))
