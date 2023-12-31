
(ns pretty-elements.toggle.views
    (:require [fruits.random.api                 :as random]
              [metamorphic-content.api           :as metamorphic-content]
              [pretty-elements.toggle.attributes :as toggle.attributes]
              [pretty-elements.toggle.prototypes :as toggle.prototypes]
              [pretty-presets.api                :as pretty-presets]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- toggle
  ; @ignore
  ;
  ; @param (keyword) toggle-id
  ; @param (map) toggle-props
  ; {}
  [toggle-id {:keys [content href on-click placeholder] :as toggle-props}]
  [:div (toggle.attributes/toggle-attributes toggle-id toggle-props)
        [(cond href :a on-click :button :else :div)
         (toggle.attributes/toggle-body-attributes toggle-id toggle-props)
         [metamorphic-content/compose content placeholder]]])

(defn element
  ; @param (keyword)(opt) toggle-id
  ; @param (map) toggle-props
  ; {:border-color (keyword or string)(opt)
  ;  :border-position (keyword)(opt)
  ;  :border-radius (map)(opt)
  ;   {:all, :tl, :tr, :br, :bl (keyword, px or string)(opt)}
  ;  :border-width (keyword, px or string)(opt)
  ;  :class (keyword or keywords in vector)(opt)
  ;  :content (metamorphic-content)(opt)
  ;  :cursor (keyword or string)(opt)
  ;   Default: :pointer
  ;  :disabled? (boolean)(opt)
  ;  :fill-color (keyword or string)(opt)
  ;  :fill-pattern (keyword)(opt)
  ;   Default: :cover
  ;  :height (keyword, px or string)(opt)
  ;  :hover-color (keyword or string)(opt)
  ;  :hover-effect (keyword)(opt)
  ;  :href (string)(opt)
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :marker-color (keyword or string)(opt)
  ;  :marker-position (keyword)(opt)
  ;  :on-click (function or Re-Frame metamorphic-event)(opt)
  ;  :on-right-click (Re-Frame metamorphic-event)(opt)
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :placeholder (metamorphic-content)(opt)
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)
  ;  :target (keyword)(opt)
  ;   :blank, :self
  ;  :width (keyword, px or string)(opt)}
  ;
  ; @usage
  ; [toggle {...}]
  ;
  ; @usage
  ; [toggle :my-toggle {...}]
  ([toggle-props]
   [element (random/generate-keyword) toggle-props])

  ([toggle-id toggle-props]
   ; @note (tutorials#parametering)
   (fn [_ toggle-props]
       (let [toggle-props (pretty-presets/apply-preset              toggle-props)
             toggle-props (toggle.prototypes/toggle-props-prototype toggle-props)]
            [toggle toggle-id toggle-props]))))
