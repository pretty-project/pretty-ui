
(ns pretty-website.follow-us.utils)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn provider->fa-icon-class
  ; @ignore
  ;
  ; @param (keyword) provider
  ;
  ; @example
  ; (provider->fa-icon-class :instagram)
  ; =>
  ; "fa-instagram"
  ;
  ; @return (string)
  [provider]
  (->> provider (name)
                (str "fa-")
                (keyword)))
