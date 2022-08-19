
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-tools.image-preloader.sample
    (:require [x.app-tools.api :as tools]))



;; -- Az applikáció indításának várakoztatása egy kép sikeres letöltéséig -----
;; ----------------------------------------------------------------------------

(defn my-image
  []
  [tools/image-preloader {:uri "/my-image.png"}])
