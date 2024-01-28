
(ns pretty-elements.toggle.prototypes
    (:require [dom.api              :as dom]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn toggle-props-prototype
  ; @ignore
  ;
  ; @param (keyword) toggle-id
  ; @param (map) toggle-props
  ; {:border-color (keyword or string)(opt)
  ;  :href (string)(opt)
  ;  :marker-color (keyword or string)(opt)
  ;  :on-click-f (function)(opt)}
  ;
  ; @return (map)
  ; {:border-position (keyword)
  ;  :border-width (keyword, px or string)
  ;  :click-effect (keyword)
  ;  :focus-id (keyword)
  ;  :marker-position (keyword)}
  [toggle-id {:keys [border-color href marker-color on-click-f] :as toggle-props}]
  (merge {:focus-id toggle-id}
         (if marker-color {:marker-position :tr})
         (if border-color {:border-position :all
                           :border-width    :xxs})
         (if href         {:click-effect :opacity})
         (if on-click-f   {:click-effect :opacity})
         (-> toggle-props)
         (if href       {:on-mouse-up dom/blur-active-element!})
         (if on-click-f {:on-mouse-up dom/blur-active-element!})))
