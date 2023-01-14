
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
  ;  :position (keyword)
  ;  :size (keyword)}
  [_ {:keys [illustration position size]}]
  (let [illustration-url (str "/illustrations/" (name illustration) ".png")]
       [:div.c-illustration {:data-position-absolute position}
                            [:div.c-illustration--body {:style {:background-image (css/url illustration-url)}
                                                        :data-element-height size
                                                        :data-element-width  size}]]))

(defn component
  ; @param (keyword)(opt) illustration-id
  ; @param (map) illustration-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :illustration (keyword)
  ;  :position (keyword)(opt)
  ;   :tl, :tr, :br, :bl
  ;   Default: :br
  ;  :size (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;   Default: :xxl
  ;  :style (map)(opt)}
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
