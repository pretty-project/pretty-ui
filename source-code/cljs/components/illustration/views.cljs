
(ns components.illustration.views
    (:require [components.illustration.prototypes :as illustration.prototypes]
              [fruits.css.api                     :as css]
              [fruits.random.api                  :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- illustration
  ; @param (keyword) illustration-id
  ; @param (map) illustration-props
  ; {:height (keyword, px or string)
  ;  :uri (string)
  ;  :width (keyword, px or string)}
  [_ {:keys [height uri width]}]
  [:div.c-illustration [:div.c-illustration--inner {:style {:background-image (css/url uri)}
                                                    :data-element-height height
                                                    :data-element-width  width}]])

(defn view
  ; @param (keyword)(opt) illustration-id
  ; @param (map) illustration-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :height (keyword, px or string)(opt)
  ;   Default: :xxl
  ;  :uri (string)
  ;  :style (map)(opt)
  ;  :width (keyword, px or string)(opt)
  ;   Default: :xxl}
  ;
  ; @usage
  ; [illustration {...}]
  ;
  ; @usage
  ; [illustration :my-illustration {...}]
  ([illustration-props]
   [view (random/generate-keyword) illustration-props])

  ([illustration-id illustration-props]
   ; @note (tutorials#parameterizing)
   (fn [_ illustration-props]
       (let [illustration-props (illustration.prototypes/illustration-props-prototype illustration-props)]
            [illustration illustration-id illustration-props]))))
