
(ns pretty-website.multi-menu.prototypes)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn menu-props-prototype
  ; @ignore
  ;
  ; @param (keyword) menu-id
  ; @param (map) menu-props
  ;
  ; @return (map)
  ; {}
  [_ menu-props]
  ; By default threshold is set to 0, and the menu items visible independently
  ; from the viewport width.
  (merge {:threshold 0}
         (-> menu-props)))
