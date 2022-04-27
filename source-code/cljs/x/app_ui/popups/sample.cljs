
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.popups.sample
    (:require [x.app-core.api :as a]))



;; -- Names -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @name {:layout :boxed}
;  A popup felületén megjelenő tartalmat magában foglaló DIV elem háttere az applikáció témája szerinti háttérszín
;
; @name {:layout :unboxed}
;  TODO ...
;
; @name {:layout :flip}
;  TODO ...
;
; @name {:render-exclusive? true}
;  A popup elem renderelése előtt bezárja az összes látható popup elemet és látható surface elemet



;; -- Popup elem megjelenítése ------------------------------------------------
;; ----------------------------------------------------------------------------

(defn my-header
  [popup-id]
  "My header")

(defn my-body
  [popup-id]
  "My body")

(a/reg-event-fx
  :add-my-popup!
  [:ui/render-popup! :my-popup
                     {:body   #'my-body
                      :header #'my-header}])



;; -- Popup elem bezárása -----------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :close-my-popup!
  [:ui/close-popup! :my-popup])
