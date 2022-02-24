
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-locales.name-handler.engine)



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (map)
(def NAME-ORDERS {:en :normal
                  :hu :reversed})



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn name->ordered-name
  ; @param (string) first-name
  ; @param (string) last-name
  ; @param (keyword) locale-id
  ;
  ; @return (string)
  [first-name last-name locale-id]
  (let [name-order (get NAME-ORDERS locale-id)]
       (if (= name-order :reversed)
           (str last-name  " " first-name)
           (str first-name " " last-name))))
