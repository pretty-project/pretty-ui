
(ns elements.ghost.views
    (:require [random.api                :as random]
              [elements.ghost.helpers    :as ghost.helpers]
              [elements.ghost.prototypes :as ghost.prototypes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- ghost-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) ghost-id
  ; @param (map) ghost-props
  [ghost-id ghost-props]
  [:div.e-ghost--body (ghost.helpers/ghost-body-attributes ghost-id ghost-props)])

(defn ghost
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) ghost-id
  ; @param (map) ghost-props
  [ghost-id ghost-props]
  [:div.e-ghost (ghost.helpers/ghost-attributes ghost-id ghost-props)
                [ghost-body                     ghost-id ghost-props]])

(defn element
  ; @param (keyword)(opt) ghost-id
  ; @param (map) ghost-props
  ; {:border-radius (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;   Default: :s
  ;  :class (keyword or keywords in vector)(opt)
  ;  :indent (map)(opt)
  ;   {:bottom (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    :left (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    :right (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    :top (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl}
  ;  :height (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;   Default: :s
  ;  :outdent (map)(opt)
  ;  :style (map)(opt)}
  ;
  ; @usage
  ; [ghost {...}]
  ;
  ; @usage
  ; [ghost :my-ghost {...}]
  ([ghost-props]
   [element (random/generate-keyword) ghost-props])

  ([ghost-id ghost-props]
   (let [ghost-props (ghost.prototypes/ghost-props-prototype ghost-props)]
        [ghost ghost-id ghost-props])))
