
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-tools.image-loader.sample
    (:require [x.app-tools.api :as tools]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn my-image
  []
  [tools/image-loader {:uri "/my-image.png"}])