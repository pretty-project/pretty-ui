
(ns pretty-elements.vertical-line.views
    (:require [fruits.random.api                        :as random]
              [pretty-elements.vertical-line.attributes :as vertical-line.attributes]
              [pretty-elements.vertical-line.prototypes :as vertical-line.prototypes]
              [pretty-presets.api                       :as pretty-presets]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element
  ; @param (keyword)(opt) line-id
  ; @param (map) line-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :color (keyword or string)(opt)
  ;   Default: :muted
  ;  :height (keyword, px or string)(opt)
  ;   Default: :parent
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)
  ;  :strength (px)(opt)
  ;   Default: 1}
  ;
  ; @usage
  ; [vertical-line {...}]
  ;
  ; @usage
  ; [vertical-line :my-vertical-line {...}]
  ([line-props]
   [element (random/generate-keyword) line-props])

  ([line-id line-props]
   ; @note (tutorials#parametering)
   (fn [_ line-props]
       (let [line-props (pretty-presets/apply-preset                   line-props)
             line-props (vertical-line.prototypes/line-props-prototype line-props)]
            [:div (vertical-line.attributes/line-attributes line-id line-props)
                  [:div (vertical-line.attributes/line-body-attributes line-id line-props)]]))))
