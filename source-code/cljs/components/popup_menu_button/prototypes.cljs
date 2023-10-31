
(ns components.popup-menu-button.prototypes)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn button-props-prototype
  ; @param (map) button-props
  ;
  ; @return (map)
  ; {}
  [button-props]
  (merge {:border-radius    {:all :s}
          :font-size        :xs
          :gap              :xs
          :horizontal-align :left
          :hover-color      :highlight
          :icon-size        :m
          :indent           {:horizontal :xxs :vertical :xxs}
          :width            :auto}

          ; A VERTICAL outdent már tulságosan megköti/szabályozza a komponens használatát!
          ; Deprecated!
          ;:outdent          {:vertical :xs}}
         (-> button-props)))
