
(ns pretty-elements.toggle.prototypes
    (:require [pretty-build-kit.api :as pretty-build-kit]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn toggle-props-prototype
  ; @ignore
  ;
  ; @param (keyword) toggle-id
  ; @param (map) toggle-props
  ; {:border-color (keyword or string)(opt)}
  ;  :marker-color (keyword or string)(opt)}
  ;
  ; @return (map)
  ; {:border-position (keyword)
  ;  :border-width (keyword, px or string)
  ;  :focus-id (keyword)
  ;  :marker-position (keyword)}
  [toggle-id {:keys [border-color marker-color] :as toggle-props}]
  (merge {:focus-id toggle-id}
         (if marker-color {:marker-position :tr})
         (if border-color {:border-position :all
                           :border-width    :xxs})
         (-> toggle-props)))
