
(ns pathom.errors)



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn error-answer?
  ; @param (*) n
  ;
  ; @usage
  ;  (pathom/error-answer? {:error/id "..."})
  ;
  ; @return (boolean)
  [n]
  (and (map? n)
       (string? (get n :error/id))))

(defn correct-answer?
  ; @param (*) n
  ;
  ; @usage
  ;  (pathom/correct-answer? {:error/id "..."})
  ;
  ; @return (boolean)
  [n]
  (let [error-answer? (error-answer? n)]
       (not error-answer?)))
