
(ns pretty-elements.notification-bubble.prototypes
    (:require [react-references.api :as react-references]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn bubble-props-prototype
  ; @ignore
  ;
  ; @param (keyword) bubble-id
  ; @param (map) bubble-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [bubble-id {:keys [border-color] :as bubble-props}]
  (let [set-reference-f (react-references/set-reference-f bubble-id)])

  (merge {:font-size   :s
          :font-weight :medium
          :text-color  :default}
         (if border-color {:border-position :all
                           :border-width    :xxs})
         (-> bubble-props)))
         ; text unselectable
         ;(pretty-properties/default-size-props {:size-unit :full-block})
         ;(pretty-properties/default-wrapper-size-props {})))
