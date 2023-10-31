
(ns components.user-avatar.prototypes)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn avatar-props-prototype
  ; @ignore
  ;
  ; @param (map) avatar-props
  ;
  ; @return (map)
  ; {:colors (strings in vector)
  ;  :icon (keyword)
  ;  :icon-family (keyword)
  ;  :size (px)}
  [avatar-props]
  (merge {:colors      ["var( --color-muted )"]
          :icon        :person
          :icon-family :material-symbols-outlined
          :size        60}
         (-> avatar-props)))
