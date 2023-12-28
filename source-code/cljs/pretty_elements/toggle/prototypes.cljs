
(ns pretty-elements.toggle.prototypes
    (:require [pretty-build-kit.api :as pretty-build-kit]
              [fruits.noop.api :refer [return]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn toggle-props-prototype
  ; @ignore
  ;
  ; @param (map) toggle-props
  ; {:border-color (keyword or string)(opt)}
  ;  :marker-color (keyword)(opt)}
  ;
  ; @return (map)
  ; {:border-position (keyword)
  ;  :border-width (keyword, px or string)
  ;  :marker-position (keyword)}
  [{:keys [border-color marker-color] :as toggle-props}]
  (merge {:content-value-f return
          :placeholder-value-f return}
         (if marker-color {:marker-position :tr})
         (if border-color {:border-position :all
                           :border-width    :xxs})
         (-> toggle-props)))
