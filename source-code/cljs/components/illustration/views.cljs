
(ns components.illustration.views
    (:require [components.illustration.prototypes :as illustration.prototypes]
              [css.api                            :as css]
              [random.api                         :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- illustration
  ; @param (keyword) illustration-id
  ; @param (map) illustration-props
  ; {:illustration (keyword)
  ;  :height (keyword)
  ;  :width (keyword)}
  [_ {:keys [illustration height width]}]
  (let [illustration-url (str "/illustrations/" (name illustration) ".webp")]
       [:div.c-illustration [:div.c-illustration--body {:style {:background-image (css/url illustration-url)}
                                                        :data-element-height height
                                                        :data-element-width  width}]]))

(defn component
  ; @param (keyword)(opt) illustration-id
  ; @param (map) illustration-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :height (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;   Default: :xxl
  ;  :illustration (keyword)
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
   (let [illustration-props (illustration.prototypes/illustration-props-prototype illustration-props)]
        [illustration illustration-id illustration-props])))
