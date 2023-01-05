
(ns website.navbar.helpers
    (:require [x.environment.api :as x.environment]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn scroll-f
  ; @param (boolean) intersecting?
  [intersecting?]
  (x.environment/set-element-attribute! "mt-navbar" "data-scrolled" (not intersecting?)))
