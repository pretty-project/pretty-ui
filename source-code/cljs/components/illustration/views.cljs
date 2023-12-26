
(ns components.illustration.views
    (:require [components.illustration.prototypes :as illustration.prototypes]
              [fruits.css.api                     :as css]
              [fruits.random.api                  :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- illustration
  ; @param (keyword) illustration-id
  ; @param (map) illustration-props
  ; {:height (keyword)
  ;  :uri (string)
  ;  :width (keyword)}
  [_ {:keys [height uri width]}]
  [:div.c-illustration [:div.c-illustration--body {:style {:background-image (css/url uri)}
                                                   :data-element-height height
                                                   :data-element-width  width}]])

(defn component
  ; @param (keyword)(opt) illustration-id
  ; @param (map) illustration-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :height (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;   Default: :xxl
  ;  :uri (string)
  ;  :style (map)(opt)
  ;  :width (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;   Default: :xxl}
  ;
  ; @usage
  ; [illustration {...}]
  ;
  ; @usage
  ; [illustration :my-illustration {...}]
  ([illustration-props]
   [component (random/generate-keyword) illustration-props])

  ([illustration-id illustration-props]
   ; @note (tutorials#parametering)
   (fn [_ illustration-props]
       (let [illustration-props (illustration.prototypes/illustration-props-prototype illustration-props)]
            [illustration illustration-id illustration-props]))))
