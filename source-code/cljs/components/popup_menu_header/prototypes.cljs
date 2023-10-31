
(ns components.popup-menu-header.prototypes)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header-props-prototype
  ; @param (map) header-props
  ; {:close-button (map)(opt)
  ;  :label (map)(opt)}
  ;
  ; @return (map)
  ; {:close-button (map)
  ;  :label (map)}
  [{:keys [close-button label] :as header-props}]
  (merge {}
         (-> header-props)
         (if close-button {:close-button (merge {:border-radius {:all :s}
                                                 :hover-color   :highlight
                                                 :icon          :close
                                                 :keypress      {:key-code 27}}
                                                (-> close-button))})
         (if label        {:label        (merge {:color     :muted
                                                 :font-size :xs
                                                 :indent    {:horizontal :xs :left :s}}
                                                (-> label))})))
