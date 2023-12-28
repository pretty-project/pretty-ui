
(ns pretty-elements.ghost.views
    (:require [fruits.random.api                :as random]
              [pretty-elements.ghost.attributes :as ghost.attributes]
              [pretty-elements.ghost.prototypes :as ghost.prototypes]
              [pretty-presets.api               :as pretty-presets]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn ghost
  ; @ignore
  ;
  ; @param (keyword) ghost-id
  ; @param (map) ghost-props
  [ghost-id ghost-props]
  [:div (ghost.attributes/ghost-attributes ghost-id ghost-props)
        [:div (ghost.attributes/ghost-body-attributes ghost-id ghost-props)]])

(defn element
  ; @param (keyword)(opt) ghost-id
  ; @param (map) ghost-props
  ; {:border-radius (map)(opt)
  ;   {:all, :tl, :tr, :br, :bl (keyword, px or string)(opt)}
  ;  :class (keyword or keywords in vector)(opt)
  ;  :indent (map)(opt)
  ;   {:bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :height (keyword, px or string)(opt)
  ;   Default: :s
  ;  :outdent (map)(opt)
  ;   {:bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)
  ;  :width (keyword, px or string)(opt)
  ;   Default: :s}
  ;
  ; @usage
  ; [ghost {...}]
  ;
  ; @usage
  ; [ghost :my-ghost {...}]
  ([ghost-props]
   [element (random/generate-keyword) ghost-props])

  ([ghost-id ghost-props]
   ; @note (tutorials#parametering)
   (fn [_ ghost-props]
       (let [ghost-props (pretty-presets/apply-preset            ghost-props)
             ghost-props (ghost.prototypes/ghost-props-prototype ghost-props)]
            [ghost ghost-id ghost-props]))))
