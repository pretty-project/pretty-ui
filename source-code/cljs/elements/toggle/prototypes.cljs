
(ns elements.toggle.prototypes
    (:require [noop.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn toggle-props-prototype
  ; @ignore
  ;
  ; @param (map) toggle-props
  ; {:border-color (keyword or string)(opt)}
  ;  :cursor (keyword)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :marker-color (keyword)(opt)}
  ;
  ; @return (map)
  ; {:border-position (keyword)
  ;  :border-width (keyword)
  ;  :cursor (keyword)
  ;  :height (keyword)
  ;  :hover-color (keyword)
  ;  :marker-position (keyword)
  ;  :width (keyword)}
  [{:keys [border-color cursor disabled? marker-color] :as toggle-props}]
  ; XXX#5603 (source-code/cljs/elements/button/prototypes.cljs)
  (merge {:height :auto
          :width  :content}
         (if marker-color {:marker-position :tr})
         (if border-color {:border-position :all
                           :border-width    :xxs})
         (param toggle-props)
         (if disabled? {:cursor      (or cursor :default)
                        :hover-color :none}
                       {:cursor      :pointer})))
