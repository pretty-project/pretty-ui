
(ns templates.item-lister.core.helpers)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn order-by-label-f
  ; @param (namespaced keyword) order-by
  ;
  ; @example
  ; (order-by-label-f :name/ascending)
  ; =>
  ; :by-name-ascending
  ;
  ; @return (keyword)
  [order-by]
  ; Az order-by-label-f függvény az {:order-by ...} tulajdonság értékéből elkészíti,
  ; a hozzá tartozó címke szövegét.
  ; Pl. :name/ascending => :by-name-ascending => "Név szerint (növekvő)"
  (keyword (str "by-" (namespace order-by)
                "-"   (name      order-by))))
