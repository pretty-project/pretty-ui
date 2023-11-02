
(ns pretty-elements.ghost.views
    (:require [pretty-elements.ghost.attributes :as ghost.attributes]
              [pretty-elements.ghost.prototypes :as ghost.prototypes]
              [pretty-presets.api        :as pretty-presets]
              [random.api                :as random]))

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
  ;   {:tl (keyword)(opt)
  ;    :tr (keyword)(opt)
  ;    :br (keyword)(opt)
  ;    :bl (keyword)(opt)
  ;    :all (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
  ;  :class (keyword or keywords in vector)(opt)
  ;  :indent (map)(opt)
  ;   {:bottom (keyword)(opt)
  ;    :left (keyword)(opt)
  ;    :right (keyword)(opt)
  ;    :top (keyword)(opt)
  ;    :horizontal (keyword)(opt)
  ;    :vertical (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
  ;  :height (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;   Default: :s
  ;  :outdent (map)(opt)
  ;   Same as the :indent property.
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)
  ;  :width (keyword)(opt)
  ;   :auto, :parent, :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;   Default: :auto}
  ;
  ; @usage
  ; [ghost {...}]
  ;
  ; @usage
  ; [ghost :my-ghost {...}]
  ([ghost-props]
   [element (random/generate-keyword) ghost-props])

  ([ghost-id ghost-props]
   (fn [_ ghost-props] ; XXX#0106 (README.md#parametering)
       (let [ghost-props (pretty-presets/apply-preset            ghost-props)
             ghost-props (ghost.prototypes/ghost-props-prototype ghost-props)]
            [ghost ghost-id ghost-props]))))
