
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-environment.scroll-handler.subs)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn scrolled-to-top?
  ; @usage
  ;  (r environment/scrolled-to-top? db)
  ;
  ; @return (boolean)
  [db _]
  (let [scrolled-to-top? (get-in db [:environment :sroll-handler/meta-items :scrolled-to-top?])]
       (boolean scrolled-to-top?)))
