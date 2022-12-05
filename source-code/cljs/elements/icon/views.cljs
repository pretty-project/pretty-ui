
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.icon.views
    (:require [elements.icon.helpers    :as icon.helpers]
              [elements.icon.prototypes :as icon.prototypes]
              [random.api               :as random]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- icon
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) icon-id
  ; @param (map) icon-props
  ; {:icon (keyword)}
  [icon-id {:keys [icon] :as icon-props}]
  [:div.e-icon (icon.helpers/icon-attributes icon-id icon-props)
               [:i.e-icon--body (icon.helpers/icon-body-attributes icon-id icon-props)
                                icon]])

(defn element
  ; @param (keyword)(opt) icon-id
  ; @param (map) icon-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :color (keyword or string)(opt)
  ;   :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
  ;   Default: :default
  ;  :icon (keyword)
  ;  :icon-family (keyword)(opt)
  ;   :material-icons-filled, :material-icons-outlined
  ;   Default: :material-icons-filled
  ;  :indent (map)(opt)
  ;   {:bottom (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    :left (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    :right (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    :top (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl}
  ;  :layout (keyword)(opt)
  ;   :touch-target Az ikont tartalmazó elem méretei megegyeznek az icon-button típus méreteivel
  ;  :size (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;   Default: :m
  ;  :style (map)(opt)}
  ;
  ; @usage
  ; [icon {...}]
  ;
  ; @usage
  ; [icon :my-icon {...}]
  ([icon-props]
   [element (random/generate-keyword) icon-props])

  ([icon-id icon-props]
   (let [icon-props (icon.prototypes/icon-props-prototype icon-props)]
        [icon icon-id icon-props])))
